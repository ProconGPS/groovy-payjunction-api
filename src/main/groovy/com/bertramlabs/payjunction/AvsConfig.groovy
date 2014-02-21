package com.bertramlabs.payjunction

class AvsConfig {
	boolean useAddress
	boolean useZip
	boolean bypass
	String operator = ''

	static final String address = 'ADDRESS'
	static final String zip = 'ZIP'

	def verify(addr) {
		useAddress = addr == address
		useZip = addr == zip
		this
	}

	def and(addr) {
		determineOperands(addr, 'AND')
		this
	}

	def or(addr) {
		determineOperands(addr, 'OR')
		this
	}

	def bypass() {
		clear()
		bypass = true
		this
	}

	def off() {
		clear()
		this
	}

	def clear() {
		useAddress = false
		useZip = false
		bypass = false
		operator = ''
	}

	def build() {
		if(bypass) {
			return 'BYPASS'
		} else if(useAddress || useZip) {
			if((useAddress && useZip) && !operator) {
				throw new IllegalStateException("Operator required if both 'address' and 'zip' are specified'")
			}
			def parts = []
			if(useAddress) {
				parts << address
			}
			if(useZip) {
				parts << zip
			}
			def joinStr = ' '
			if(operator) {
				joinStr = "_${operator}_"
			}
			return parts.join(joinStr)
		} else {
			return 'OFF'
		}
	}

	def determineOperands(arg, op) {
		if(arg == address) {
			useAddress = true
		} else if(arg == zip) {
			useZip = true
		}
		operator = op
	}
}