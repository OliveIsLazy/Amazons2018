from threading import Thread
from rpyc.utils.server import ThreadedServer
import rpyc
from time import sleep
from argparse import ArgumentParser


class Brain:
    def __init__(self):
        self.survival_time = 0
        self.input = None
        self.best = None

    def handle_communicator_input(self, board: str):
        self.input = board

        self.find_best_action()

    def find_best_action(self):
        self.best = self.input[::-1]


class MyService(rpyc.Service):
    def exposed_handle_input(self, board: str):
        return brain.handle_communicator_input(board)

    def exposed_get_brain_response(self):
        return brain.best


server = ThreadedServer(MyService, port=12345)
t = Thread(target=server.start)
t.daemon = True
t.start()

parser = ArgumentParser()
parser.add_argument("algorithm")
args = parser.parse_args()  # contains which algorithm to use in the future

brain = Brain()

while True:
    brain.survival_time += 1
    sleep(5)
