# esign-poc
esign poc app main repository

### Guide
 - Run mvn clean jetty:run => web app start at https://localhost:8080

### Notice
 - After user sign and enter info, the api can not return the form data because user need to confirm their signature by click on email-link
 - After user click on confirm email, admin need to go to `https://localhost:8080/getInfo` to get data (in csv format),
 - Sample: 
> "completed","email","role","first","last","title","company","agreementId","topmostSubform.Page1.f1_1","topmostSubform.Page1.f1_2"
    "2015-03-31 11:43:32","phuonghqh@gmail.com","SIGNER","phuong","Huynh","","","2AAABLblqZhDlCeuFaX58M19rDBprJNQi1ityJGqmWbVGiTEgOu1fC3gQwD7-U-sX17Akp4chPWU*","fd","dfs"

### Dosusign Support
 - Create a free account at [Docusign Developer](https://www.docusign.com/developer-center)
 - Create an integration token at [Integration Token](https://admindemo.docusign.com/api-integrator-key)
 - Update values in `src/main/resources/docusign-auth.json` and `src/main/resources/application.properties`
 - Create an document template with name at : [Docusign Template](https://appdemo.docusign.com/templates/all)
 - Consider demo account and production account
 - Look at console log to see the process