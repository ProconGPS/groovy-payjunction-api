# groovy-payjunction-api
Groovy wrapper for the PayJunction API

## Features
So far only transactions have been implemented: charges, refunds, voids, holds, and captures.  You can create new transactions, update existing ones, and get details of transaction history.  Other features to come!

## Testing the API
PayJunction offers a [development sandbox](http://developer.payjunction.com/documentation/development-and-test-account-information/) that doesn't require you to sign-in.  A convenience method is available for testing with this sandbox:

```
PayJunction.testInstance()
```

## Examples
A simple charge to a credit card, using the TEST card:

```
def session = PayJunction.testInstance()
session.newTransaction {
	creditCard TEST_CARD
	charge amount: 110.0
}
```

Break out some of the charges (optional) and add some billing info (address and zip are the expected test values):

```
session.newTransaction {
	creditCard TEST_CARD
	charge amount:110.00, tax: 9.72, shipping: 5.00
	billingInfo {
		address '8320'
		zip '85284'
	}
}
```

Turn on AVS:

```
session.newTransaction {
	creditCard TEST_CARD
	charge amount:110.00, tax: 9.72, shipping: 5.00
	billingInfo {
		address '8320'
		zip '85284'
	}
	avs {
		verify address and zip
	}
}
```

Get a list of your transactions:

```
session.transactions
```

...or one in particular:

```
session.transaction(1111)
```

You can update a transaction, for example changing an amount:

```
session.updateTransaction(1111) {
	capture 100.0
}
```

Or simply void an unsettled transaction:

```
session.updateTransaction(1111) {
	doVoid()
}
```

## TODOS
1. Add first-class vault support
1. Add some configurability for defaults, e.g. turn AVS or CVV on by default.
