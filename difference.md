# ข้อแตกต่าง Servlet vs Reactive (WebFlux) 

|   title    | Servlet   | Reactive (WebFlux) |
|------------| --------- | -------------------|
|   Web Server    | Tomcat  | Netty    | 
|      I/O        | Blocking | Non-Blocking | 
|   Request Processing   | Sychronous (Thread Pool) | Asychronous  (Event Loop) | 
| Http Request | javax.servlet.http.HttpServletRequest | org.springframework.http.server.reactive.ServerHttpRequest | 
| Web Filter   | org.springframework.web.filter.OncePerRequestFilter | org.springframework.web.server.WebFilter | 
