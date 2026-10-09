package tech.dawn.template.testdsl.util

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction

/**
 * Utility to print the accessibility traversal order of a Compose hierarchy.
 */
object TraversalDebugHelper {

    /**
     * Prints the traversal order to the console.
     */
    fun printTraversalOrder(interaction: SemanticsNodeInteraction) {
        val root = interaction.fetchSemanticsNode()
        val result = mutableListOf<TraversalNodeInfo>()
        collectNodesRecursively(root, result, 0)

        val output = buildString {
            appendLine("\n--- ACCESSIBILITY TRAVERSAL ORDER ---")
            result.forEach { node ->
                val indent = "  ".repeat(node.depth)
                val indexLabel = node.index?.toString() ?: "MISSING"
                val groupMarker = if (node.isGroup) "[GROUP]" else ""
                val missingFlag = if (node.isGroup && node.index == null) "⚠️ INDEX MISSING" else ""

                appendLine("$indent- ${node.description} $groupMarker [Index: $indexLabel] $missingFlag")
            }
            appendLine("--------------------------------------\n")
        }
        println(output)
    }

    private fun collectNodesRecursively(node: SemanticsNode, result: MutableList<TraversalNodeInfo>, depth: Int) {
        val isGroup = node.config.getOrNull(SemanticsProperties.IsTraversalGroup) ?: false
        val index = node.config.getOrNull(SemanticsProperties.TraversalIndex)

        val contentDescription = node.config.getOrNull(SemanticsProperties.ContentDescription)?.joinToString()
        val text = node.config.getOrNull(SemanticsProperties.Text)?.joinToString()
        val editableText = node.config.getOrNull(SemanticsProperties.EditableText)?.text
        val testTag = node.config.getOrNull(SemanticsProperties.TestTag)

        val description = contentDescription ?: editableText ?: text ?: testTag ?: "Node (${node.id})"

        val hasMeaningfulSemantics = contentDescription != null || text != null || editableText != null || testTag != null

        // Add node if it's a group or has meaningful semantics to show in the tree
        if (isGroup || hasMeaningfulSemantics) {
            result.add(TraversalNodeInfo(description, index, isGroup, depth))
        }

        // Sort children by traversal index to simulate accessibility order
        val children = node.children.sortedBy {
            it.config.getOrNull(SemanticsProperties.TraversalIndex) ?: Float.MAX_VALUE
        }

        // Increment depth only if the current node was added to the printout
        val nextDepth = if (isGroup || hasMeaningfulSemantics) depth + 1 else depth
        children.forEach { collectNodesRecursively(it, result, nextDepth) }
    }

    private data class TraversalNodeInfo(
        val description: String,
        val index: Float?,
        val isGroup: Boolean,
        val depth: Int
    )
}
