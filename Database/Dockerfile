FROM microsoft/mssql-server-linux:latest
ENV ACCEPT_EULA Y
ENV SA_PASSWORD 8jkGh47hnDw89Haq8LN2
ENV MSSQL_PID Developer
RUN apt-get install mssql-tools
RUN ln -sfn /opt/mssql-tools/bin/sqlcmd /usr/bin/sqlcmd

COPY ./create-db.sql /
COPY ./entrypoint.sh /

ENTRYPOINT [ "/bin/bash", "entrypoint.sh" ]
CMD [ "/opt/mssql/bin/sqlservr" ]
