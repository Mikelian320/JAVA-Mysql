pipeline {
  agent {
    docker {
      image 'maven-ssh:v1.0'
      args '-u node -m 1G -v /home/jenkins/.ssh:/home/root/.ssh -v /etc/localtime:/etc/localtime'
    }
  }
  stages {
    stage('showBranchName') {
      steps {
        sh 'echo ${BRANCH_NAME}'
      }
    }

    stage('Compile') {
      steps {
        sh 'mvn package'
      }
    }

    stage('Publish') {
      steps {
        sh 'chmod 700 target/demo-0.0.1-SNAPSHOT.jar'
        sh 'chmod 700 restartSpringBoot.sh'
        sh 'ssh autojenkins@www.greatwebtech.cn "mkdir -p java/${BRANCH_NAME}"'
        sh 'scp target/demo-0.0.1-SNAPSHOT.jar autojenkins@www.greatwebtech.cn:~/java/${BRANCH_NAME}'
        sh 'scp restartSpringBoot.sh autojenkins@www.greatwebtech.cn:~/java/${BRANCH_NAME}'
      }
    }

    stage('Restart') {
      steps {
        sh 'ssh autojenkins@www.greatwebtech.cn "./java/${BRANCH_NAME}/restartSpringBoot.sh ./java/${BRANCH_NAME}/demo-0.0.1-SNAPSHOT.jar ${BRANCH_NAME}"'
      }
    }

  }
}
