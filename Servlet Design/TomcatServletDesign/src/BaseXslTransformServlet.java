import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import ParserUtility.DataParsers.DataHandler;
import ParserUtility.DataParsers.FileDataHandler;

public abstract class BaseXslTransformServlet extends MyHttpServlet{
	protected DataHandler<Document> xmlDataHandler;
	protected String xslPath;
	protected String xmlPath;
	@Override
	protected void initiate(ServletContext context) throws Exception {
		xmlPath = context.getRealPath(XmlContext.XML_NAME);
		xslPath = context.getRealPath(getXslFileName());
		xmlDataHandler = new FileDataHandler(xmlPath);
	}
	
	protected abstract String getXslFileName();
	
	@Override
	protected void extractParameter(HttpServletRequest request) throws Exception {
		// hook method
	}


	@Override
	protected String executeAndGetResult() throws Exception {
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer transformer = tranFactory.newTransformer
				( new StreamSource(xslPath));
		transformConfig(transformer);
	    StringWriter writer = new StringWriter();
		Source src = getXmlStreamSource();
		Result dest = new StreamResult(writer);
		transformer.transform(src, dest);
		return writer.toString();
	}
	
	protected void transformConfig(Transformer transformer){
		//hook method
	}
	
	protected Source getXmlStreamSource() throws Exception{
		//hook method
		StringReader reader = new StringReader(xmlDataHandler.getDataString());
		return new StreamSource(reader);
	}

}