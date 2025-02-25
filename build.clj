(ns build
  (:require
   [clojure.tools.build.api :as b]))

;;; See also https://clojure.org/guides/tools_build
;;; To install jar:  clj -T:build all-jar

(def lib 'com.github.arachne-framework/aristotle)
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]
                :scm {:url "https://github.com/TheFakeLorLyons/aristotle-build"
                      :connection "scm:git:git://github.com/TheFakeLorLyons/aristotle-build" 
                      :tag (b/git-process {:git-args "rev-parse HEAD"})}
                :pom-data [[:description "Description of your library"]
                           [:url "https://github.com/TheFakeLorLyons/aristotle-build"]
                           [:licenses
                            [:license
                             [:name "Eclipse Public License 2.0"]
                             [:url "https://www.eclipse.org/legal/epl-2.0/"]]]
                           [:developers
                            [:developer
                             [:name "Melody"]
                             [:email "wistfulmelodylispfulprogram@gmail.com"]]]]})

  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (println "Installing: class-dir =" class-dir "version = " version)
  (let [opts {:lib lib :basis basis :jar-file jar-file :class-dir class-dir :version version}]
    (b/install opts)))

(defn deploy [_]
  (println "Deploying version" version "to Clojars")
  (b/deploy {:class-dir class-dir
             :lib lib
             :version version
             :basis basis
             :jar-file jar-file
             :repository "clojars"
             :sign-releases? false}))

(defn all-jar [_]
  (clean nil) 
  (jar nil) 
  (install nil))

(defn release [_]
  (clean nil)
  (jar nil)
  (deploy nil))