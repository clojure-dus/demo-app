(ns demo-app.aleph.hello-tcp
  (:use [aleph.tcp :only [start-tcp-server tcp-client]]
        [lamina.core :only [enqueue channel read-channel close map* receive receive-all siphon]]
        [lamina.viz :only [view-graph]]
        [gloss.core :only [string]]))

; messages enqueued in this channel will be sent to all clients
(defonce broadcast (channel))

(defn broadcast-registration-handler
  "register a client to the broadcast channel"
  [client-channel client-info]
  (println "registering client" client-info)
  (siphon broadcast client-channel))

(defn start-broadcast-server
  "start a server that listens for connections on port 10000"
  []
  (start-tcp-server (var broadcast-registration-handler)
                    {:port 10000
                     :frame (string :utf-8 :delimiters ["\n"])}))

(defn connect
  "connect to the broadcast server - returns a channel that contains the broadcast messages"
  []
  @(tcp-client {:host "localhost"
                :port 10000
                :frame (string :utf-8 :delimiters ["\n"])}))

; the server
(defonce server (start-broadcast-server))

; two connections
(defonce c1 (connect))
(defonce c2 (connect))

; registering functions that are called for each broadcast message
;(receive-all c1 #(println "c1 received:" %))
;(receive-all c2 #(println "c2 received:" %))

; broadcasting a message
;(enqueue broadcast "it works!")