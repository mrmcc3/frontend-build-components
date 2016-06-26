(ns fbc.sass
  (:require [com.stuartsierra.component :as component]
            [libsass.build :as sass]
            [hawk.core :as hawk]))

(defrecord SassCompiler [source-paths autobuild compiler-opts watcher]
  component/Lifecycle
  (start [this]
    (sass/build source-paths compiler-opts)
    (if (and autobuild (not watcher))
      (assoc this
        :watcher (hawk/watch! [{:paths   source-paths
                                :filter  (fn [_ {:keys [file]}] (sass/sass-file? file))
                                :handler (fn [_ _] (sass/build source-paths compiler-opts))}]))
      this))
  (stop [this]
    (when watcher (hawk/stop! watcher))
    (sass/clean source-paths compiler-opts)
    this))

(defn sass-compiler [cfg]
  (map->SassCompiler
    {:source-paths  (-> cfg :sass :source-paths)
     :autobuild     (-> cfg :sass :autobuild)
     :compiler-opts (-> cfg :sass :compiler)}))
