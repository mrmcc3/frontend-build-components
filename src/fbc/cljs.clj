(ns fbc.cljs
  (:require [com.stuartsierra.component :as component]
            [clojure.java.io :as io]
            [cljs.build.api :as cljs]
            [cljs.env :as env]))

(defn clean [{:keys [output-to output-dir]}]
  (when (and output-to output-dir)
    (doseq [file (cons (io/file output-to)
                       (reverse (file-seq (io/file output-dir))))]
      (when (.exists file) (.delete file)))))

(defrecord CljsCompiler [inputs opts watch stop-fn started]
  component/Lifecycle
  (start [this]
    (if started
      this
      (do
        (cljs/build inputs opts)
        (assoc this
          :started true
          :stop-fn (when watch
                     (let [stop (atom nil)
                           cenv (if-not (nil? env/*compiler*)
                                  env/*compiler*
                                  (env/default-compiler-env opts))
                           futr (future (cljs/watch inputs opts cenv stop))]
                       (fn []
                         (reset! stop true)
                         (future-cancel futr))))))))
  (stop [this]
    (when stop-fn (stop-fn))
    (when started (clean opts))
    (assoc this :started nil :stop-fn nil)))

(defn cljs-compiler [cfg]
  (map->CljsCompiler
    {:inputs (apply cljs/inputs (get-in cfg [:cljs :source-paths]))
     :opts   (get-in cfg [:cljs :compiler])
     :watch  (get-in cfg [:cljs :watch])}))
