package com.bertramlabs.payjunction

class PayJunctionError {
	def message
	def parameter
	def type

	static buildFromResponse(response) {
		response.errors.collect {err ->
			new PayJunctionError(message: err.message, parameter: err.parameter, type: err.type)
		}
	}
}