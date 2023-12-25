pipeline {
    agent {
        label('test-agent')
    }
	parameters {
		hidden(name: 'hidden_param', defaultValue: 'hidden_value', description: 'Hidden parameter')
	}
    stages {
        stage('Stage') {
            steps {
                echo 'Hello World'
            }
        }
    }
}
