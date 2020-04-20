pipeline {
  agent {
    docker {
      image 'maven'
      args '-m 1G -v /home/jenkins/.ssh:/home/root/.ssh'
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
        sh 'scp target/SearchMysql-jar-with-dependencies.jar autojenkins@www.greatwebtech.cn:~/'
      }
    }

  }
}