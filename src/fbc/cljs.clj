(ns fbc.cljs
  (:require [com.stuartsierra.component :as component]
            [suspendable.core :refer [Suspendable]]
            [clojure.java.io :as io]
            [cljs.build.api :as cljs]))

(defn clean [{:keys [output-to output-dir]}]
  (when (and output-to output-dir)
    (doseq [file (cons (io/file output-to)
                       (reverse (file-seq (io/file output-dir))))]
      (when (.exists file) (.delete file)))))

(defrecord CljsCompiler [inputs opts]
  component/Lifecycle
  (start [this]
    (cljs/build inputs opts)
    this)
  (stop [this]
    (clean opts)
    this)
  Suspendable
  (suspend [this]
    this)
  (resume [this old]
    (when-not (= (:opts this) (:opts old))
      (component/stop old))
    (component/start this)))

(defn cljs-compiler [cfg]
  (map->CljsCompiler
    {:inputs (apply cljs/inputs (get-in cfg [:cljs :source-paths]))
     :opts   (get-in cfg [:cljs :compiler])}))
