(ns fbc.figwheel
  (:require [figwheel-sidecar.repl-api :as fig]
            [com.stuartsierra.component :as component]
            [suspendable.core :refer [Suspendable]]))

(defrecord Figwheel [config autobuild]
  component/Lifecycle
  (start [this]
    (when-not (fig/figwheel-running?)
      (fig/start-figwheel! config))
    (when-not autobuild
      (fig/build-once "default"))
    this)
  (stop [this]
    (when (fig/figwheel-running?)
      (when autobuild
        (fig/stop-autobuild "default"))
      (fig/clean-builds "default")
      (fig/stop-figwheel!))
    this)
  Suspendable
  (suspend [this]
    (when (fig/figwheel-running?)
      (when autobuild
        (fig/stop-autobuild "default")))
    this)
  (resume [this old]
    (if (fig/figwheel-running?)
      (if (= (:config this) (:config old))
        (if autobuild
          (fig/start-autobuild "default")
          (fig/build-once "default"))
        (do (component/stop old) (component/start this)))
      (component/start this))
    this))

(defn figwheel [cfg]
  (let [autobuild (get-in cfg [:figwheel :autobuild])
        build-id (if autobuild "default" "no-autobuild")
        build (-> (:cljs cfg)
                  (assoc :id "default" :figwheel autobuild)
                  (update-in [:compiler :optimizations] #(if (nil? %1) :none %1)))
        options (dissoc (:figwheel cfg) :autobuild)]
    (map->Figwheel
      {:autobuild autobuild
       :config    {:figwheel-options options
                   :build-ids        [build-id]
                   :all-builds       [build]}})))
