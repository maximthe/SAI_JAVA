import matplotlib.pyplot as plt
import numpy as np
# Depths
depths = list(range(3, 14))

# Node counts
minimax_nodes = [47, 123, 337, 827, 2017, 5043, 12919, 31643, 76701, 187645, 460349]
alphabeta_nodes = [25, 44, 85, 138, 247, 388, 805, 1203, 2051, 3155, 6177]

# Reduction factor
def linear_reduction_factor(minimax_nodes, alphabeta_node):

    return np.mean([j/i for i, j in zip(minimax_nodes, alphabeta_node)])

print(linear_reduction_factor(minimax_nodes, alphabeta_nodes) * 100)

# Plot
plt.figure(figsize=(10, 6))
plt.plot(depths, minimax_nodes, marker='o', label="Minimax")
plt.plot(depths, alphabeta_nodes, marker='o', label="Alpha-Beta Pruning")
plt.yscale("log")  # log scale to show difference better
plt.xlabel("Search Depth")
plt.ylabel("Nodes Visited (log scale)")
plt.title("Minimax vs Alpha-Beta Pruning Node Count")
plt.legend()
plt.grid(True, which="both", linestyle="--", linewidth=0.5)
plt.show()