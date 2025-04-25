# volunteer-plus

## System requirements

- Windows or a Unix-like OS that Docker supports
- Docker

## Getting started

### 1. Create an env file

Create a file called `.env` in the root of the repository. Check the `.env.example` file for an example.

**NOTE!**
If you don't have all credentials for different services like DB, s3 etc. Please, reach out to developers for them.

### 2. Start the application

Run a command depending on your OS:

Windows:

```
.\bin\start.bat
```

Unix:

```
./bin/start.sh
```

**NOTE!**

Our project is using a Quartz, so when you start your project the first time you should set  
initialize quartz schema to always, and only then you can turn it to never so all jobs and  
triggers after restart would be saved!!!  

```
# in order to keep records in db after restart use 'never' value here
# in order to initialize schema on first run please switch it to 'always'
spring.quartz.jdbc.initialize-schema=never
```


**NOTE!**
In case you want to run FE locally you should take a look at this config: 

```javascript
const volunteerPlusApiService = new VolunteerPlusApiService({
  basePath: import.meta.env.VITE_VOLUNTEER_PLUS_API_BASE_URL,
  // uncomment for local testing
  // basePath: 'http://localhost:8080/api',
});
```

**NOTE! Webhook proxy tool**
```
npx localtunnel --port 8080
```


## *Qualification work links:*

[Ходаков Максим](https://1drv.ms/w/c/3e16880802f568e1/EbbHDUjhrIREltFyA_RS76EBSOJXvjmghFp1JzPhcQFzTg)
