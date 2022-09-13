import unittest

class TestMethods(unittest.TestCase):

    def test_mult(self):
        self.assertEqual(42, 6 * 7)

if __name__ == '__main__':
    unittest.main()