package com.bertramlabs.payjunction

import static java.net.HttpURLConnection.*

class HttpRequest {
	def server
	def userName
	def password
	def apiKey

	def get(urlString) {
		def conn = setupConnection("${server}${urlString}")
		conn.requestMethod = 'GET'
		conn.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
		checkResponseCode(conn)
		readResponse(conn)
	}

	def post(urlString, formData) {
		def conn = setupConnection("${server}${urlString}")
		conn.requestMethod = 'POST'

		conn.setDoOutput(true)
		conn.setDoInput(true)

		conn.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
		def os = new BufferedWriter(new OutputStreamWriter(conn.outputStream))
		def urlEncoded = buildUrlEncodedFormData(formData)
		os.write(urlEncoded)
		os.close()

		// request has been opened so check the response code
		checkResponseCode(conn)
		readResponse(conn)
	}

	def put(urlString, formData) {
		def conn = setupConnection("${server}${urlString}")
		conn.requestMethod = 'PUT'

		conn.setDoOutput(true)
		conn.setDoInput(true)
		
		conn.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
		def os = new BufferedWriter(new OutputStreamWriter(conn.outputStream))
		def urlEncoded = buildUrlEncodedFormData(formData)
		os.write(urlEncoded)
		os.close()

		checkResponseCode(conn)
		readResponse(conn)
	}

	def delete(urlString) {
		checkState()
		def conn = setupConnection("${server}${urlString}")
		conn.requestMethod = 'DELETE'

		if(conn.responseCode != HTTP_NO_CONTENT) {
			checkResponseCode(conn)
		}
	}

	private buildUrlEncodedFormData(formData) {
		formData.collect {k,v ->
			v = "${v}"
			"${k}=${URLEncoder.encode(v, 'UTF-8')}"
		}.join('&')
	}

	private setupConnection(urlString) {
		checkState()
		def url = new URL(urlString)
		def conn = url.openConnection()

		String creds = "${userName}:${password}".toString().bytes.encodeBase64().toString()
		conn.setRequestProperty("Authorization", "Basic ${creds}".toString())
		conn.setRequestProperty("Accept", "application/json".toString())
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded".toString())
		if(apiKey) {
			conn.setRequestProperty('X-PJ-Application-Key', apiKey.toString())
		}

		conn
	}

	private checkState() {
		if(!server) {
			throw new NotConfiguredException("server not set")
		}
		if(!userName) {
			throw new NotConfiguredException("userName not set")
		}
		if(!password) {
			throw new NotConfiguredException("password not set")
		}
	}

	private readResponse(conn) {
		readStream(conn, 'inputStream')
	}

	private readError(conn) {
		readStream(conn, 'errorStream')
	}

	private readStream(conn, stream) {
		def br = new BufferedReader(new InputStreamReader(conn."$stream"))
		def textBuf = new StringBuilder()
		def text
		while((text = br.readLine()) != null) {
			textBuf.append(text)
		}
		textBuf.toString()
	}

	private checkResponseCode(conn) {
		switch(conn.responseCode) {
		case HTTP_OK:
		return
		case HTTP_BAD_REQUEST:
		throw new BadRequestException(readError(conn))
		case HTTP_BAD_METHOD:
		throw new MethodNotAllowedException(readError(conn))
		case HTTP_UNAUTHORIZED:
		throw new UnauthorizedException(readError(conn))
		default:
		throw new PayJunctionException(readError(conn))
		}
	}
}
