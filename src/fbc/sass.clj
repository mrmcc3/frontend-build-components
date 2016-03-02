(ns fbc.sass
  (:require [com.stuartsierra.component :as component]
            [libsass.build :as sass]))

(defrecord SassCompiler [source-paths compiler-opts watch stop-fn started]
  component/Lifecycle
  (start [this]
    (if started
      this
      (do
        (sass/build source-paths compiler-opts)
        (assoc this
          :started true
          :stop-fn (when watch (sass/watch source-paths compiler-opts))))))
  (stop [this]
    (when stop-fn (stop-fn))
    (when started (sass/clean source-paths compiler-opts))
    (assoc this :started nil :stop-fn nil)))

(defn sass-compiler [cfg]
  (map->SassCompiler
    {:source-paths  (get-in cfg [:sass :source-paths])
     :compiler-opts (get-in cfg [:sass :compiler])
     :watch         (get-in cfg [:sass :watch])}))

