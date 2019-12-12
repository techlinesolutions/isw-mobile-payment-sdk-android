# Interswitch Payment SDK

This library aids in processing payment through the following channels
- [x] Card
- [x] Verve Wallet
- [x] QR Code
- [ ] USSD (Coming soon)
- [ ] PAYCODE (Coming soon)


# Usage
There are three steps you would have to complete to set up the SDK and perform transaction
 - Install the SDK as a dependency
 - Configure the SDK with Merchant Information
 - Initiate payment with customer details



#### Installation

To install the project, add the following to your root project's `build.gradle`

```groovy

    allprojects {
        repositories {
            google()
            jcenter()
            maven { url "http://dl.bintray.com/techquest/maven-repo" }
        }
        
        //... other stuff
    }

```

Then add the following to the your app project's `build.gradle`

```groovy
    
    dependencies {
        def versionName = 'latest-version'
        implementation "com.interswitchng:isw-mobile-payment-sdk:$versionName"
    }
```

now build the project.


#### Configuration
You would also need to configure the project with your merchant credentials.

```java

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // configure sdk
        configureSDK();
    }

    public void configureSDK() {
        // use provided configuration for your merchant account
        String merchantId = "<your merchantId>"; 
        String merchantCode = "<your merchantCode>";
        String merchantKey = "<your merchantKey>";

        // create sdk configuration
        IswSdkConfig config = new IswSdkConfig(merchantId, 
                        merchantKey, merchantCode, "566");

        // uncomment to set environment, default is Environment.TEST
        // config.setEnv(Environment.PRODUCTION);
        
        // initialize sdk at boot of application
        IswMobileSdk.initialize(this, config);
    }
}
```
Once the SDK has been initialized, you can then perform transactions.


#### Performing Transactions
You can perform a transaction, once the SDK is configured, by providing the payment info and a payment callback, like so:

```java
    public class PurchaseActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback  {
        
        @Override
        protected void onCreate() {
            //.. other stuff
            payButton.setOnclickListener((v) -> {
                initiatePayment();
            });
        }
        
        
        @Override
        public void onUserCancel() {
            // called when the user cancels
        }
    
        @Override
        public void onPaymentCompleted(IswPaymentResult result) {
            // called when the transaction was completed (success/failure)
        }
        
        private void initiatePayment() {
            // set customer info
            String customerId = "<customer-id>",
                    customerName = "<customer-name>",
                    customerEmail = "<customer.email@domain.com>",
                    customerMobile = "<customer-phone>",
                    // generate a unique random
                    // reference for each transaction
                    reference = "<your-unique-ref>";
                        
            // amount in kobo e.g. "N500.00" -> 50000
            long amount = providedAmount; // e.g. 50000
                
            // create payment info
            IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(customerId,
                    customerName, customerEmail, customerMobile,
                    currencyCode, reference, amount);

            // trigger payment with info and payment-callback
            IswMobileSdk.getInstance().pay(iswPaymentInfo, this);
        }
        
    }

```
#### Handling Result
To handle result all you need to do is handle the result in the callback methods: whenever the user cancels, `IswPaymentCallback.onUserCancel` is called, and when the transaction is complete, `IswPaymentCallback.onPaymentCompleted` is called with the result: an instance of `IswPaymentResult`

| Field                 | Type          | meaning  |   
|-----------------------|---------------|----------|
| responseCode          | String        | txn response code  |
| responseDescription   | String        | txn response code description |
| isSuccessful          | boolean       | flag indicates if txn is successful  |
| transactionReference  | String        | reference for txn  |
| amount                | int           | txn amount  |
| channel               | PaymentChannel| channel used to make payment: one of `CARD, QR, USSD, PAYCODE  |



```java

public class PurchaseActivity extends AppCompatActivity implements IswMobileSdk.IswPaymentCallback  {
    // ... other stuff

    @Override
    public void onUserCancel() {
        toast("You cancelled payment, please try again.");
    }

    @Override
    public void onPaymentCompleted(IswPaymentResult result) {
        if (result.isSuccessful)
            toast("your payment was successful, using: " + result.channel.name());
        else toast("unable to complete payment at the moment, try again later");
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}


```
And that is it you can start processing payment in your android app.
