(defproject mrmcc3/frontend-build-components "0.1.2-SNAPSHOT"
  :description "Components for building front-end projects with clojure"
  :url "https://github.com/mrmcc3/frontend-build-components"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["clojars" {:sign-releases false}]]
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.9.36" :scope "provided"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [com.stuartsierra/component "0.3.1"]
                 [suspendable "0.1.1"]
                 [figwheel-sidecar "0.5.3-2"]
                 [mrmcc3/libsass-clj "0.1.5"]])
