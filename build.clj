(ns build
  (:require
   [clojure.tools.build.api :as b]
   #_[deps-deploy.deps-deploy :as dd]))

;;; See also https://clojure.org/guides/tools_build
;;; To install jar:  clj -T:build all-jar
(def lib 'com.github.thefakelorlyons/aristotle)
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]

  (println "Building the jar.")

  (b/write-pom {:class-dir class-dir
                :lib lib 
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/copy-dir {:src-dirs ["src"] :target-dir class-dir})
  (b/jar {:class-dir class-dir :jar-file jar-file}))

(defn install [_]
  (println "Installing: class-dir =" class-dir "version = " version)
  (let [opts {:lib lib :basis basis :jar-file jar-file :class-dir class-dir :version version}]
    (b/install opts)))

#_(defn deploy [_]
  (dd/deploy {:installer :remote
              :artifact jar-file
              :pom-file (b/pom-path {:lib lib :class-dir class-dir})}))

(defn all-jar [_]
  (clean nil) 
  (jar nil) 
  (install nil))

(defn release [_]
  (clean nil)
  (jar nil)
  #_(deploy nil))