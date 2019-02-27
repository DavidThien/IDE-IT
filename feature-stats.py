from git import Repo
import time
import subprocess
import os

repo = Repo('.')
commits_itr = repo.iter_commits()
days = []
last_commit = None
last_date = time.strftime("%Y, %m, %d", time.gmtime(repo.iter_commits().next().authored_date))

# Get the last commit of each day
for commit in commits_itr:
    next_date = time.strftime("%Y, %m, %d", time.gmtime(commit.authored_date))
    if next_date != last_date:
        days.append((last_commit, next_date))
    last_date = next_date
    last_commit = commit

# Copy the tests we want to run for use later
subprocess.call(['cp', '-r', 'backend_plugin/src/test/java/negatives', './'])

git = repo.git
days.reverse()

day_results = []
total_tests = 0
for (commit, day) in days:
    git.checkout(commit.hexsha)
    subprocess.Popen(['cp', '-r', 'negatives', 'backend_plugin/src/test/java/negatives'], stdout=subprocess.PIPE)

    # cd into the backend_plugin dir and run the mvn test
    wd = os.getcwd()
    os.chdir("backend_plugin")
    subprocess.call(['mvn', '-Dtest=*Negative', 'surefire-report:report'])
    os.chdir(wd)
    stdout = process.communicate()[0]

    result_line = None
    for line in stdout:
        if 'Tests run: ' in line:
            result_line = line
            break

    results = [int(s) for s in str.split() if s.isdigit()]
    total_tests = results[0]
    failures = results[1]
    errors = results[2]
    skipped = results[3]
    passed = total_tests - failures
    day_results.append(passed)

# Checkout back to the head of our branch
git.checkout('feature-testing')

print(day_results)

# Delete the tests we copied
subprocess.call(['rm', '-rf', 'negatives'])
