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
session = com.bertramlabs.payjunction.PayJunction.testInstance()
session.transactions.create {
	creditCard TEST_CARD
	charge amount: 110.00
}
```

Break out some of the charges (optional) and add some billing info (address and zip are the expected test values):

```
session.transaction.create {
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
session.transactions.create {
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
session.transactions.all()
```

...or one in particular:

```
session.transactions.get(1111)
```

You can update a transaction, for example changing an amount:

```
session.transaction(1111).update {
	capture 100.0
}
```

Or simply void an unsettled transaction:

```
session.transaction(1111).update {
	doVoid()
}
```

Create a customer:

```
session.customers.create {
	companyName 'My Company'
	phone '3332221111'
	email 'me@example.com'
}
```

Get a customer's vaults:
```
session.customers[123].vaults
```

Create a new transaction using a vault:
```
	session.transactions.create {
		vault 1234
		charge amount:110.00, tax: 9.72, shipping: 5.00
	}
```
## TODOS
1. Add some configurability for defaults, e.g. turn AVS or CVV on by default.
