(ns demo-app.aleph.hello-http
  (:use [aleph.http :only [start-http-server wrap-ring-handler wrap-aleph-handler]]
        [lamina.core :only [enqueue channel close map* receive receive-all]]))

(defn ring-hello-world [req]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body "Hello, World!"})

;(def hello-world (wrap-ring-handler ring-hello-world))

(defn hello-world [channel request]
  (enqueue channel
    {:status 200
     :headers {"content-type" "text/html"}
     :body "hello from aleph"}))

(defonce server
  (start-http-server (var hello-world) {:port 8080}))