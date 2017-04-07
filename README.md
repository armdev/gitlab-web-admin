# gitlab-web-admin
web application for manage gitlab projects

How to run?

1. Start MongoDB.
2. Build/Deploy/Run gitlab-web in Apache Tomcat 8/9.
3. Sign Up with your gitlab username and private token https://gitlab.com/profile/account , also put gitlab url.
4. Sign in.
5. Load your projects from gitlab, if project have yml file, you can build and have a look logs from admin.
6. Send trigger job, new pipeline.
7. Access http://localhost:{Tomcat Port}/gitlab-web/index.jsf
