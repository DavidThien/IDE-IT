from git import Repo
import time
import subprocess

repo = Repo('.')
commits_itr = repo.iter_commits()
days = []
last_commit = None
last_date = time.strftime("%Y, %j", time.gmtime(repo.iter_commits().next().authored_date))

# Get the last commit of each day
for commit in commits_itr:
    next_date = time.strftime("%Y, %j", time.gmtime(commit.authored_date))
    if next_date != last_date:
        days.append((last_commit, next_date))
    last_date = next_date
    last_commit = commit

# Copy the tests we want to run for use later
subprocess.call(['cp', '-r', 'backend_plugin/src/test/java/negatives', './'])

git = repo.git

for (commit, day) in days:
    git.checkout(commit.hexsha)
    print("Checked out " + commit.hexsha + " for day " + day)

git.checkout('HEAD')

#subprocess.call(['mvn', '-Dtest=*Negative', 'surefire-report:report'])

# Delete the tests we copied
subprocess.call(['rm', '-rf' 'negatives'])
