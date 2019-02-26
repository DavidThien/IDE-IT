from git import Repo
import time
import subprocess

repo = Repo('.')
commits_itr = repo.iter_commits()
days = []
last_commit = None
last_date = time.strftime("%Y, %j", time.gmtime(repo.iter_commits().next().authored_date))

for commit in commits_itr:
    next_date = time.strftime("%Y, %j", time.gmtime(commit.authored_date))
    if next_date != last_date:
        days.append((last_commit, next_date))
    last_date = next_date
    last_commit = commit

subprocess.run(['cp', 'backend_plugin/test/feature-tests' './'])
subprocess.run(['mvn -Dtest=*FalseNegative surefire-report:report'])

for commit in days:
    print(commit)
