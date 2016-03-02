(ns fbc.figwheel
  (:require [figwheel-sidecar.repl-api :as f]
            [com.stuartsierra.component :as c]))

(defn autobuild? [cfg build]
  (-> (group-by :id (:all-builds cfg)) (get build) first :figwheel))

(defrecord Figwheel [cfg profile]
  c/Lifecycle
  (start [this]
    (when-not (f/figwheel-running?)
      (f/start-figwheel! cfg))
    (if (autobuild? cfg profile)
      (f/start-autobuild profile)
      (f/build-once profile))
    this)
  (stop [this]
    (when (f/figwheel-running?)
      (f/stop-autobuild profile)
      (f/clean-builds profile)
      (f/stop-figwheel!))
    this))

(defn figwheel [{:keys [figwheel profile]}]
  (Figwheel. (assoc figwheel :build-ids [:no-autobuild-on-start]) profile))

