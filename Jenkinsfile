pipeline {
  agent {
    docker {
      image 'maven-ssh:v1.0'
      args '-u node -m 1G -v /home/jenkins/.ssh:/home/root/.ssh'
    }
  }
  stages {
    stage('Compile') {
      steps {
        sh 'mvn assembly:assembly'
      }
    }

    stage('Publish') {
      steps {
        sh 'scp target/Spring-web-search-jar-with-dependencies.jar autojenkins@www.greatwebtech.cn:~/'
      }
    }

    stage('Restart') {
      steps {
        sh 'ssh autojenkins@www.greatwebtech.cn "./restartSearchMysql.sh Spring-web-search-jar-with-dependencies.jar"'
      }
    }

  }
}
