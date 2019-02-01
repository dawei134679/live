package com.mpig.api.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferedResponse extends HttpServletResponseWrapper {
	public static final int OT_NONE = 0, OT_WRITER = 1, OT_STREAM = 2;

	private BufferedOutputStream outputStream = null;
	private PrintWriter writer = null;
	private int outputType = OT_NONE;

	public BufferedResponse(HttpServletResponse response) {
		super(response);
		outputStream = new BufferedOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		if (outputType == OT_STREAM)
			throw new IllegalStateException();
		else if (outputType == OT_WRITER)
			return writer;
		else {
			outputType = OT_WRITER;
			writer = new PrintWriter(new OutputStreamWriter(outputStream, getCharacterEncoding()), true);
			return writer;
		}
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (outputType == OT_WRITER)
			throw new IllegalStateException();
		else if (outputType == OT_STREAM)
			return outputStream;
		else {
			outputType = OT_STREAM;
			return outputStream;
		}
	}

	public void flushBuffer() throws IOException {
		try {
			writer.flush();
		} catch (Exception e) {
		}
		try {
			outputStream.flush();
		} catch (Exception e) {
		}
	}

	public void reset() {
		outputType = OT_NONE;
		outputStream.reset();
	}

	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return outputStream.toByteArray();
	}
}