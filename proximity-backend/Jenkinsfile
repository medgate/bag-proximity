#!/usr/bin/env groovy
@Library('jenkins-pipeline-library@develop')
import com.openwt.jenkins.pipeline.lib.*

properties([
    [$class: 'jenkins.model.BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']],
    disableConcurrentBuilds()
])

final String JENKINS_SLAVE_BUILD = 'linux'
final String APP_NAME = 'coronapp-backend'
final String SDT_DOCKER_REGISTRY = 'registry.openwt.com'
final String dockerTagPrefix = "$SDT_DOCKER_REGISTRY/coronapp"
final String dockerImageName = "${dockerTagPrefix}/${APP_NAME}"

String initialVersion
String artifactVersion
String dockerTag

final def slackCi = new Slack('#bag-covid19-dev-ci', this)
final def git = new Git(this, CommandType.SH)
final def gitBranch = new GitBranch(this)

final boolean shouldRelease = gitBranch.currentIsMaster() || gitBranch.currentStartsWith('support/')
//final boolean shouldDeployDev = gitBranch.currentIsDevelop()


timestamps {
  node(JENKINS_SLAVE_BUILD) {
    try {
      env.JAVA_HOME = "${tool 'jdk-11.0.1'}"
      env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
      sh 'java -version'

      stage('Clean & Checkout') {
        cleanWs()
        checkout scm
        stash name: 'sources', exclude: 'build/*'
        notifyBitbucket()
        slackCi.send "🚧 ${APP_NAME} BUILD STARTED"
      }

      stage('Prepare') {
        def props = readProperties file: 'gradle.properties'
        initialVersion = props['version']
        if (shouldRelease) {
          artifactVersion = initialVersion.replace('-SNAPSHOT', "")
        } else {
          artifactVersion = initialVersion + "-${git.getShortCommit()}"
        }
        dockerTag = "${dockerImageName}:${artifactVersion}"
        echo "Artifact version: $artifactVersion"
        echo "Docker tag: $dockerTag"
      }

      withEnv(["ORG_GRADLE_PROJECT_version=${artifactVersion}"]) {
        stage('Build JAR') {
          sh './gradlew clean build '
          slackCi.send "🚧 Build JAR gate passed"
        }

        if (gitBranch.currentIsDevelop() || shouldRelease) {
          stage('SonarQube Analysis') {
            withEnv(["SONAR_PROJECT_VERSION=${initialVersion}"]) {
              withSonarQubeEnv('sonar.openwt.com') {
                sh "./gradlew sonarqube"
                slackCi.send "✅ SonarQube gate passed"
              }
            }
          }
        }

        stage('Build Docker') {
          withCredentials([usernamePassword(credentialsId: 'coronapp-jenkins', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh "docker login -u $USERNAME -p $PASSWORD ${SDT_DOCKER_REGISTRY}"
          }
          sh "docker build -t $dockerTag ."
          if (gitBranch.currentIsDevelop()) {
            sh "docker build -t $dockerImageName:latest ."
          }
          slackCi.send "🐳 Build Docker gate passed"
        }

        if (shouldRelease) {
          stage('Git Push and Tag') {
            git.createAndPushTag(artifactVersion)
          }
        }

        stage('Push to Docker Registry') {
          withCredentials([usernamePassword(credentialsId: 'coronapp-jenkins', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh "docker login -u $USERNAME -p $PASSWORD ${SDT_DOCKER_REGISTRY}"
          }
          sh "docker push $dockerTag"
          slackCi.send "🐳 Pushed Docker image to OWT registry (tag: ${dockerTag})"
          if (gitBranch.currentIsDevelop()) {
            sh "docker push $dockerImageName:latest"
            slackCi.send "🐳 Pushed Docker image to OWT registry (tag: ${dockerImageName}:latest)"
          }
        }
      }

      currentBuild.result = 'SUCCESS'
      slackCi.send "🏁 ${APP_NAME} BUILD SUCCESS"
      notifyBitbucket()
    } catch (e) {
      currentBuild.result = 'FAILURE'
      if (shouldRelease) {
        slackCi.send "It looks like someone broke the build pipeline, beers on him! 🍻"
      }
      notifyBitbucket()
      throw e
    }
  }
}
