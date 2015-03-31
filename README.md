# esign-poc
esign poc app main repository

### Guide
 - Run mvn clean jetty:run => web app start at https://localhost:8080

### Notice
After user sign and enter info, the api can not return the form data because user need to confirm their signature by click on email-link
=> After user click on confirm email, admin need to go to https://localhost:8080/getInfo to get data (in csv format),
ex: "completed","email","role","first","last","title","company","agreementId","topmostSubform.Page1.f1_1","topmostSubform.Page1.f1_2"
    "2015-03-31 11:43:32","phuonghqh@gmail.com","SIGNER","phuong","Huynh","","","2AAABLblqZhDlCeuFaX58M19rDBprJNQi1ityJGqmWbVGiTEgOu1fC3gQwD7-U-sX17Akp4chPWU*","fd","dfs"