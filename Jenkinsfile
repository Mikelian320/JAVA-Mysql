pipeline {
  agent {
    docker {
      image 'node:12.14'
      args '-m 1G -v /home/jenkins/.ssh:/home/node/.ssh'
    }

  }
  stages {
    stage('Install') {
      steps {
        dir(path: '"${env.WORKSPACE}/src/web"') {
          sh 'npm install'
        }

      }
    }

    stage('Build') {
      steps {
        dir(path: '"${env.WORKSPACE}/src/web"') {
          sh 'export NODE_OPTIONS=--max_old_space_size=1000 && npm run build'
        }

      }
    }

    stage('Publish') {
      steps {
        dir(path: '"${env.WORKSPACE}/src/web"') {
          sh 'scp -r build autojenkins@www.greatwebtech.cn:~/JAVA-Mysql'
        }

      }
    }

  }
}