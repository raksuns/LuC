package com.luxsky.chat.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
@MultipartConfig(location = "/Users/ias", maxFileSize = 1024 * 1024 * 10, fileSizeThreshold = 1024 * 1024, maxRequestSize = 1024 * 1024 * 20)
@WebServlet(name = "upload", urlPatterns = { "/post" })
public class ChatImageUploadServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameter("name")); // This doesn't work
		String fileName = "dummy";
		Part filePart = null;
		for (Part part : request.getParts()) {
			System.out.println(part);
			for (String headerName : part.getHeaderNames()) {
				System.out.println(headerName);
				System.out.println("-");
				System.out.println(part.getHeader(headerName));
				// To find out file name, parse header value of
				// content-disposition
				// e.g. form-data; name="file"; filename=""
			}
			// Get a normal parameter
			if (part.getName().equals("name")) {
				String paramValue = getStringFromStream(part.getInputStream());
				System.out.println(paramValue);
				fileName = paramValue;
			}
			if (part.getName().equals("file")) {
				filePart = part;
			}
		}
		filePart.write(fileName);
	}

	public String getStringFromStream(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
