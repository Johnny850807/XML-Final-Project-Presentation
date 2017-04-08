import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class MyHttpServlet extends HttpServlet{
	protected String xpath;
	protected String result;
	protected BufferedReader resultReader;
	protected BufferedWriter responseWriter;
	
	@Override
	public void init() throws ServletException {
		super.init();
		initiate(getServletContext());
	}
	
	protected abstract void initiate(ServletContext context);  // to read files or to instantiate variables.

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			log("設置步驟");
			setupConfig(request,response);
			initResponseBufferedWriter(response.getWriter());

			log("參數提取步驟");
			extractParameter(request);
			
			log("執行與得到結果步驟");
			result = executeAndGetResult(getXpathFormat());
			resultReader = new BufferedReader(new StringReader(result));
			
			log("結果為 : \n" + result);
			log("寫入伺服器輸出步驟");
			writeResultToResponse(result,responseWriter,resultReader);
			
			log("完成。");
			
		} catch (Exception e) {
			log(e.getMessage(),e);
		} finally{
			try {
				log("釋放資源。");
				close(responseWriter,resultReader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void initResponseBufferedWriter(PrintWriter responsePrintWriter){
		responseWriter = new BufferedWriter(responsePrintWriter);
	}
	
	protected void setupConfig(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String contentType = getContentType();
		String encoding  = getEncoding();
		request.setCharacterEncoding(encoding);
		response.setContentType(contentType + ";charset=" + encoding);
	}
	
	protected String getContentType(){
		return "text/html";
	}
	
	protected String getEncoding() {
		return "UTF-8";
	}

	
	protected abstract void extractParameter(HttpServletRequest request) throws Exception;
	
	protected abstract String getXpathFormat() throws Exception;
	
	protected abstract String executeAndGetResult(String xpathFormat) throws Exception;
	
	protected void writeResultToResponse(String result ,BufferedWriter responseWriter,BufferedReader resultReader) throws IOException{
		String input;
		while ((input = resultReader.readLine()) != null)
			responseWriter.write(input+"<br/>");
	}
	
	@Override
	public void destroy() {
		super.destroy();
		log("伺服器關閉");
	}
	
	protected void close(BufferedWriter responseWriter,BufferedReader resultReader) throws Exception{
		responseWriter.close();
		resultReader.close();
	}

	@Override
	public void log(String msg) {
		super.log(msg);
		System.out.println("系統快訊: " + msg);
	}
	
	
	
}
