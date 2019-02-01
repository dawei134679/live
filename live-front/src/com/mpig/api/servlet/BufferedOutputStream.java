package com.mpig.api.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class BufferedOutputStream extends ServletOutputStream {
	private ByteArrayOutputStream outputStream = null;

	public BufferedOutputStream() {
		outputStream = new ByteArrayOutputStream(1024);
	}

	/**
	 * Writes the specified byte to this output stream. The general contract for
	 * <code>write</code> is that one byte is written to the output stream. The
	 * byte to be written is the eight low-order bits of the argument
	 * <code>b</code>. The 24 high-order bits of <code>b</code> are ignored.
	 * <p>
	 * Subclasses of <code>OutputStream</code> must provide an implementation
	 * for this method.
	 * 
	 * @param b
	 *            the <code>byte</code>.
	 * @exception IOException
	 *                if an I/O error occurs. In particular, an
	 *                <code>IOException</code> may be thrown if the output
	 *                stream has been closed.
	 */
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	/**
	 * Writes <code>b.length</code> bytes from the specified byte array to this
	 * output stream. The general contract for <code>write(b)</code> is that it
	 * should have exactly the same effect as the call
	 * <code>write(b, 0, b.length)</code>.
	 * 
	 * @param b
	 *            the data.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte b[]) throws IOException {
		outputStream.write(b);
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to this output stream. The general contract for
	 * <code>write(b, off, len)</code> is that some of the bytes in the array
	 * <code>b</code> are written to the output stream in order; element
	 * <code>b[off]</code> is the first byte written and
	 * <code>b[off+len-1]</code> is the last byte written by this operation.
	 * <p>
	 * The <code>write</code> method of <code>OutputStream</code> calls the
	 * write method of one argument on each of the bytes to be written out.
	 * Subclasses are encouraged to override this method and provide a more
	 * efficient implementation.
	 * <p>
	 * If <code>b</code> is <code>null</code>, a
	 * <code>NullPointerException</code> is thrown.
	 * <p>
	 * If <code>off</code> is negative, or <code>len</code> is negative, or
	 * <code>off+len</code> is greater than the length of the array
	 * <code>b</code>, then an <tt>IndexOutOfBoundsException</tt> is thrown.
	 * 
	 * @param b
	 *            the data.
	 * @param off
	 *            the start offset in the data.
	 * @param len
	 *            the number of bytes to write.
	 * @exception IOException
	 *                if an I/O error occurs. In particular, an
	 *                <code>IOException</code> is thrown if the output stream is
	 *                closed.
	 */
	public void write(byte b[], int off, int len) throws IOException {
		outputStream.write(b, off, len);
	}

	/**
	 * Writes a <code>String</code> to the client, without a carriage
	 * return-line feed (CRLF) character at the end.
	 * 
	 * 
	 * @param s
	 *            the <code>String</code> to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */
	public void print(String s) throws IOException {
		print(s, "UTF-8");
	}

	public void print(String s, String charsetName) throws IOException {
		/*
		 * 解决中文乱码问题
		 */
		outputStream.write(s.getBytes(charsetName));
	}

	/**
	 * Writes a <code>boolean</code> value to the client, with no carriage
	 * return-line feed (CRLF) character at the end.
	 * 
	 * @param b
	 *            the <code>boolean</code> value to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void print(boolean b) throws IOException {
		print(b ? "true" : "false");
	}

	/**
	 * Writes a character to the client, with no carriage return-line feed
	 * (CRLF) at the end.
	 * 
	 * @param c
	 *            the character to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void print(char c) throws IOException {
		print(String.valueOf(c));
	}

	/**
	 * 
	 * Writes an int to the client, with no carriage return-line feed (CRLF) at
	 * the end.
	 * 
	 * @param i
	 *            the int to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void print(int i) throws IOException {
		print(String.valueOf(i));
	}

	/**
	 * 
	 * Writes a <code>long</code> value to the client, with no carriage
	 * return-line feed (CRLF) at the end.
	 * 
	 * @param l
	 *            the <code>long</code> value to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void print(long l) throws IOException {
		print(String.valueOf(l));
	}

	/**
	 * 
	 * Writes a <code>float</code> value to the client, with no carriage
	 * return-line feed (CRLF) at the end.
	 * 
	 * @param f
	 *            the <code>float</code> value to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 * 
	 */

	public void print(float f) throws IOException {
		print(String.valueOf(f));
	}

	/**
	 * 
	 * Writes a <code>double</code> value to the client, with no carriage
	 * return-line feed (CRLF) at the end.
	 * 
	 * @param d
	 *            the <code>double</code> value to send to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void print(double d) throws IOException {
		print(String.valueOf(d));
	}

	/**
	 * Writes a carriage return-line feed (CRLF) to the client.
	 * 
	 * 
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println() throws IOException {
		print("\r\n");
	}

	/**
	 * Writes a <code>String</code> to the client, followed by a carriage
	 * return-line feed (CRLF).
	 * 
	 * 
	 * @param s
	 *            the <code>String</code> to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */
	public void println(String s) {
		println(s, "UTF-8");
	}

	public void println(String s, String charsetName) {
		/*
		 * 解决中文乱码问题
		 */
		try {
			print(s, charsetName);
			println();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * Writes a <code>boolean</code> value to the client, followed by a carriage
	 * return-line feed (CRLF).
	 * 
	 * 
	 * @param b
	 *            the <code>boolean</code> value to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(boolean b) throws IOException {
		print(b);
		println();
	}

	/**
	 * 
	 * Writes a character to the client, followed by a carriage return-line feed
	 * (CRLF).
	 * 
	 * @param c
	 *            the character to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(char c) throws IOException {
		print(c);
		println();
	}

	/**
	 * 
	 * Writes an int to the client, followed by a carriage return-line feed
	 * (CRLF) character.
	 * 
	 * 
	 * @param i
	 *            the int to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(int i) throws IOException {
		print(i);
		println();
	}

	/**
	 * 
	 * Writes a <code>long</code> value to the client, followed by a carriage
	 * return-line feed (CRLF).
	 * 
	 * 
	 * @param l
	 *            the <code>long</code> value to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(long l) throws IOException {
		print(l);
		println();
	}

	/**
	 * 
	 * Writes a <code>float</code> value to the client, followed by a carriage
	 * return-line feed (CRLF).
	 * 
	 * @param f
	 *            the <code>float</code> value to write to the client
	 * 
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(float f) throws IOException {
		print(f);
		println();
	}

	/**
	 * 
	 * Writes a <code>double</code> value to the client, followed by a carriage
	 * return-line feed (CRLF).
	 * 
	 * 
	 * @param d
	 *            the <code>double</code> value to write to the client
	 * 
	 * @exception IOException
	 *                if an input or output exception occurred
	 * 
	 */

	public void println(double d) throws IOException {
		print(d);
		println();
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be
	 * written out. The general contract of <code>flush</code> is that calling
	 * it is an indication that, if any bytes previously written have been
	 * buffered by the implementation of the output stream, such bytes should
	 * immediately be written to their intended destination.
	 * <p>
	 * If the intended destination of this stream is an abstraction provided by
	 * the underlying operating system, for example a file, then flushing the
	 * stream guarantees only that bytes previously written to the stream are
	 * passed to the operating system for writing; it does not guarantee that
	 * they are actually written to a physical device such as a disk drive.
	 * <p>
	 * The <code>flush</code> method of <code>OutputStream</code> does nothing.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * Closes this output stream and releases any system resources associated
	 * with this stream. The general contract of <code>close</code> is that it
	 * closes the output stream. A closed stream cannot perform output
	 * operations and cannot be reopened.
	 * <p>
	 * The <code>close</code> method of <code>OutputStream</code> does nothing.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	public void close() throws IOException {
		outputStream.close();
	}

	/**
	 * Resets the <code>count</code> field of this byte array output stream to
	 * zero, so that all currently accumulated output in the output stream is
	 * discarded. The output stream can be used again, reusing the already
	 * allocated buffer space.
	 * 
	 * @see java.io.ByteArrayInputStream#count
	 */
	public void reset() {
		outputStream.reset();
	}

	public byte[] toByteArray() {
		return outputStream.toByteArray();
	}
}