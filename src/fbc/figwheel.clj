(ns fbc.figwheel
  (:require [figwheel-sidecar.repl-api :as fig]
            [com.stuartsierra.component :as component]))

(defrecord Figwheel [config autobuild]
  component/Lifecycle
  (start [this]
    (when-not (fig/figwheel-running?)
      (fig/start-figwheel! config))
    (when-not autobuild
      (fig/build-once :default))
    this)
  (stop [this]
    (when (fig/figwheel-running?)
      (when autobuild
        (fig/stop-autobuild :default))
      (fig/clean-builds :default)
      (fig/stop-figwheel!))
    this))

(defn figwheel [cfg]
  (let [autobuild (get-in cfg [:figwheel :autobuild])
        build-id (if autobuild :default :no-autobuild)
        build (assoc (:cljs cfg) :id :default :figwheel autobuild)
        options (dissoc (:figwheel cfg) :autobuild)]
    (map->Figwheel
      {:autobuild autobuild
       :config    {:figwheel-options options
                   :build-ids        [build-id]
                   :all-builds       [build]}})))
