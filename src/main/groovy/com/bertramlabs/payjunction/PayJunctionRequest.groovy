package com.bertramlabs.payjunction

import groovy.json.JsonSlurper

class PayJunctionRequest {
	def httpRequest
	def url = ''
	def context
	boolean idAllowed = true
	def _offset = 0

	def get(id=null) {
		def urlAppend = id ? "/${id}" : ''
		new JsonSlurper().parseText(httpRequest.get("${url}${urlAppend}"))
	}

	def create(Closure c) {
		bindClosure(c)()
		new JsonSlurper().parseText(httpRequest.post(url, context?.build()))
	}

	def update(id, Closure c) {
		bindClosure(c)()
		new JsonSlurper().parseText(httpRequest.put("${url}/${id}", context?.build()))
	}

	def update(Closure c) {
		bindClosure(c)()
		new JsonSlurper().parseText(httpRequest.put(url, context?.build()))
	}

	def delete(id=null) {
		def urlAppend = id ? "/${id}" : ''
		httpRequest.delete("${url}${urlAppend}")
	}

	def getAt(int id) {
		if(!idAllowed) {
			throw new IllegalStateException("ID cannot be set for URL ${url}")
		}
		def c = this.clone()
		c.idAllowed = false
		c.url = "${url}/${id}"
		c
	}

	def offset(int off) {
		def c = this.clone()
		c._offset = off
		c
	}

	def first() {
		def json = all()
		if(json instanceof List) {
			return json[0]
		} else {
			return json
		}
	}

	def all() {
		new JsonSlurper().parseText(httpRequest.get(url)).results
	}

	@Override
	def clone() {
		def c = new PayJunctionRequest(httpRequest: httpRequest, url: url, context: context, idAllowed: idAllowed, _offset: _offset)
		c
	}

	private bindClosure(Closure c) {
		def cloz = c
		if(context) {
			cloz = c.clone()
			cloz.delegate = context
		}
		cloz
	}
}
