package com.moore.filter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.moore.Application;
import com.moore.models.dtos.BaseResponse;
import com.moore.models.entities.RequestResult;
import com.moore.repositories.RequestResultRepository;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * ryanmoore - 9/27/16.
 */
public class AuditServletFilter implements Filter {

//    Logger logger = LoggerFactory.getLogger(AuditServletFilter.class);

    @Autowired
    private RequestResultRepository requestResultRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //nada
    }

    /**
     * This method overrides the Filter interface to intercept both incoming requests and outgoing responses.
     * We parse the request on the way in to collect input and metadata. Then, we activate the rest of the filter chain
     * with the chain.doFilter(..) call. Once that is complete, we intercept the outgoing response in order to parse the
     * results. We use the same request parameter names & response body formats so we can capture all data from
     * all requests/responses in the same way.
     *
     * @param request Incoming ServletRequest to parse for parameters & metadata.
     * @param response Outgoing ServletRequest that we wrap in order to steal it's body contents later.
     * @param chain Rest of the app's filter chain that needs to execute.
     * @throws IOException if an I/O error occurs during this filter's
     *                     processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //do stuff with request - like try to get the path & requestparams out
        RequestResult requestEntity = new RequestResult();
        requestEntity.setPath(((Request)request).getRequestURI());
        requestEntity.setInput(request.getParameter(Application.INPUT_PARAMETER));

        //now wrap the servlet response so we can peel the body out of it later.
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

        try {
            //now pass the request along - grab the uncaught errors from processing.
            chain.doFilter(request, responseCopier);
            responseCopier.flushBuffer();
        } catch (IOException | ServletException e) {

            //error situation, handle nicely
            requestEntity.setErrors(e.getClass().getName());
            requestEntity.setSuccess(false);
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            byte[] copy = responseCopier.getCopy();
            String jsonResponse = new String(copy, response.getCharacterEncoding());

            //404
            if (StringUtils.isBlank(jsonResponse)) {
                //error situation, handle nicely
                requestEntity.setErrors("Empty response body.");
                requestEntity.setSuccess(false);
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                //modify by reference is scary so we'll just return our populated entity again and save!
                requestEntity = parseResponse(jsonResponse, requestEntity);
            }
        }

        requestResultRepository.save(requestEntity);
    }

    /**
     * Helper method for parsing the response body of an outgoing ServletResponse. We peel the JSON data out of the
     * response, then serialize it to our common BaseResponse object & inspect it to determine what to save regarding the
     * request/response.
     *
     * @param jsonResponse - json peeled from the ServletResponse
     * @param requestEntity - Entity we'll be saving after we collect enoguh request/response info.
     * @return the updated requestEntity.
     */
    private RequestResult parseResponse(String jsonResponse, RequestResult requestEntity) {

        //create gson helper, try to serial, capture error if needed
        Gson gson = new Gson();
        try {
            BaseResponse serializedResponse = gson.fromJson(jsonResponse, BaseResponse.class);
            if (serializedResponse.getSuccess()) {
                requestEntity.setSuccess(true);
                requestEntity.setResult(String.valueOf(serializedResponse.getResult()));
            } else {
                requestEntity.setSuccess(false);
                requestEntity.setErrors(serializedResponse.getMessage());
            }
        } catch (JsonSyntaxException jse) {
            //error situation, handle nicely
            requestEntity.setErrors("Unable to parse response body.");
            requestEntity.setSuccess(false);
        }

        return requestEntity;
    }

    @Override
    public void destroy() {
        //no thanks
    }

    /*
     *
     * ** Below this is all dumb boilerplate code to steal the body from the HttpServletResponse! **
     *
     */
    private class HttpServletResponseCopier extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamCopier copier;

        HttpServletResponseCopier(HttpServletResponse response) throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            }

            return copier;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                copier.flush();
            }
        }

        byte[] getCopy() {
            if (copier != null) {
                return copier.getCopy();
            } else {
                return new byte[0];
            }
        }


    }

    private class ServletOutputStreamCopier extends ServletOutputStream {

        private OutputStream outputStream;
        private ByteArrayOutputStream copy;

        ServletOutputStreamCopier(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.copy = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }
}
