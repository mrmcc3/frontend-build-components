(ns fbc.libsass
  (:require [com.stuartsierra.component :as component]
            [libsass.build :as sass]))

(defrecord Libsass [cfg profile stop clean]
  component/Lifecycle
  (start [this]
    (if clean
      this
      (let [{:keys [source-paths compiler watch]}
            (get-in cfg [:all-builds profile] {})]
        (sass/build source-paths compiler)
        (assoc this
          :clean true
          :stop (when watch (sass/watch source-paths compiler))))))
  (stop [this]
    (when stop (stop))
    (when clean
      (sass/clean
        (get-in cfg [:all-builds profile :source-paths])
        (get-in cfg [:all-builds profile :compiler])))
    (assoc this :stop nil :clean nil)))

(defn xform-builds [builds]
  (into {} (map (fn [{:keys [id] :as build}] [id (dissoc build :id)])) builds))

(defn libsass [{:keys [libsass profile]}]
  (map->Libsass
    {:cfg (update libsass :all-builds xform-builds)
     :profile profile}))

