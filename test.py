import requests
from concurrent.futures import ThreadPoolExecutor

base_url = "https://3k844pkgv7.execute-api.eu-west-1.amazonaws.com/dev/"
java = base_url + "hello/sample"
graal = base_url + "hello-runtime/sample"

class Lambda:
    ls = []
    def __init__(self, url):
        self.url = url

    def get_response(self):
        self.ls.append(requests.get(self.url).elapsed.total_seconds())


java_lambda = Lambda(java)
graal_lambda = Lambda(graal)


def test_10():
    with ThreadPoolExecutor(max_workers=10) as executor:
        a = executor.submit(java_lambda.get_response())
        b = executor.submit(graal_lambda.get_response())
    print("Results are in")

for _ in range(1000):
    test_10()

def average(lst):
    return sum(lst) / len(lst)


print(f"Requests made: {len(java_lambda.ls)}")

print("Response times")
print("Java:  {:.3f}".format(average(java_lambda.ls)))
print("Graal: {:.3f}".format(average(graal_lambda.ls)))
