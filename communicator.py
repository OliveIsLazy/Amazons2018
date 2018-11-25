from argparse import ArgumentParser
from sys import version_info, executable
import rpyc

if version_info[0] > 3:
    print("Error in version number.")

parser = ArgumentParser()
parser.add_argument("board")
args = parser.parse_args()

connection = rpyc.connect("localhost", 12345)
connector = connection.root

connector.handle_input(args.board)
print(connector.get_brain_response())
