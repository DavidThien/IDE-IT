from git import Repo
import time
import subprocess
import os
import re
import matplotlib.pyplot as plt

repo = Repo('.')
curr_commit = repo.head.commit
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
# Call it feature-testing-negatives-tmp so git clean exclusion doesn't conflict with negatives
subprocess.call(['cp', '-r', 'backend_plugin/src/test/java/negatives', './feature-testing-negatives-tmp'])

git = repo.git
days.reverse()
days = days[22:]

day_results = []
day_successes = []
total_tests = 0
for (commit, day) in days:
    print("Running commit " + commit.hexsha + " for day " + day)
    git.checkout(commit.hexsha)
    subprocess.call(['rm', '-rf', 'backend_plugin/src/test/java/negatives'])
    subprocess.call(['mkdir', '-p', 'backend_plugin/src/test/java'])
    subprocess.call(['cp', '-r', 'feature-testing-negatives-tmp', 'backend_plugin/src/test/java/'])
    subprocess.call(['mv', 'backend_plugin/src/test/java/feature-testing-negatives-tmp', 'backend_plugin/src/test/java/negatives'])

    # cd into the backend_plugin dir and run the mvn test
    wd = os.getcwd()
    os.chdir("backend_plugin")
    proc = subprocess.Popen(['mvn', '-Dtest=*Negative', 'surefire-report:report'], stdout=subprocess.PIPE)
    os.chdir(wd)

    success = False
    result_line = None
    for line in iter(proc.stdout.readline, b''):
        if 'Tests run: ' in line:
            result_line = line
            success = True

    if success:
        results = re.findall(r'\b\d+\b', result_line)
        total_tests = results[0]
        failures = results[1]
        errors = results[2]
        skipped = results[3]
        passed = int(total_tests) - int(failures)
        day_results.append(passed)
        day_successes.append(day)

    git.clean(['-fde', 'feature-testing-negatives-tmp'])

# Checkout back to the head of our branch
git.checkout(curr_commit)
git.clean(['-fd'])

day_results = [0] + day_results
indices = [i + 1 for i in range(len(day_results))]

plt.plot(day_results)
plt.ylabel('total methods supported (out of ' + str(total_tests) + ')')
plt.title('BlockCommentEvaluator Successful Activation Methods')
plt.xticks(indices + [len(day_results)], [''] + day_successes)
plt.savefig('feature-support.png')
