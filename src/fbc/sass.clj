(ns fbc.sass
  (:require [com.stuartsierra.component :as component]
            [libsass.build :as sass]))

(defrecord SassCompiler [source-paths compiler-opts]
  component/Lifecycle
  (start [this]
    (sass/build source-paths compiler-opts)
    this)
  (stop [this]
    (sass/clean source-paths compiler-opts)
    this))

(defn sass-compiler [cfg]
  (map->SassCompiler
    {:source-paths  (get-in cfg [:sass :source-paths])
     :compiler-opts (get-in cfg [:sass :compiler])}))

