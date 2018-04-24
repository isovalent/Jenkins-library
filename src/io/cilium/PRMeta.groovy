package io.cilium

import org.kohsuke.github.GitHub
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHPullRequest


class PRMeta implements Serializable {

    def script

    private String pullRequest
    private String gitHubToken
    private String credentials
    private String repository

    private String GHUser
    private String GHToken

    PRMeta(script) {
        this.script = script
    }

    /*
        retuns a map with name:credentials from the Jenkins instance
    */
    def getCredentialsMap() {
        def result = [:]
        def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
            com.cloudbees.plugins.credentials.Credentials.class,
            Jenkins.instance,
            null,
            null
        );
        creds.each(){
            result["${it.id}"] = it
        }

        return result
    }

    /*
      Set GHUser and GHToken properties based on the credentials property.
    */
    def fetchCredentials() {
        def creds = getCredentialsMap()
        def GHCredentials = creds[this.credentials]
        if (GHCredentials == null) {
            throw new Exception("Cannot get credentials ${this.credentials}")
        }
        this.GHUser = GHCredentials.getUsername()
        this.GHToken = GHCredentials.getPassword()
    }

    /*
        Fetchs needed variables from the Job that are needed to retrieve
        information about the PR. It raises an exception if one of the needed
        Environment variables is not in place
    */
    def fetchAndSetEnvVariables(){
        def vars = [
            'repository': 'ghprbGhRepository',
            'pullRequest': 'ghprbPullId',
            'credentials': 'ghprbCredentialsId',
        ]

        vars.each { key, value ->
            def envVal = this.script.env.getProperty(value)
            if (envVal == null) {
                throw new Exception("cannot get env variable '${value}'")
            }
            this.metaClass."$key" = envVal
        }

        this.fetchCredentials()
    }

    /*
        Imports PR metadata from Github API
    */
    def GHPullRequest importPR(){
        this.fetchAndSetEnvVariables()
        def repo = GitHub.connect(this.GHUser, this.GHToken).getRepository(this.repository)
        return repo.getPullRequest(this.pullRequest.toInteger())
    }

    /*
        returns true if the job is trigger from a PR
    */
    def IsPR() {
        try {
            this.importPR()
            return true
        } catch(Exception e) {
            this.script.echo("IsPR:Debug: is not a PR cause '${e}'")
            return false
        }
    }

    /*
        returns the GitHub Pull Request Labels
    */
    def String[] getLabels() {
        def pr = this.importPR()
        def result = []
        pr.getLabels().each(){
            result.push(it.getName())
        }
        return result
    }
}
