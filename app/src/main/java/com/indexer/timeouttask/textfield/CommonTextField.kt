package com.indexer.timeouttask.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion

@Composable
fun CommonTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  label: String? = null,
  isError: Boolean = false,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  onImeActionPerformed: Any,
) {
  TextField(//this is testing
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    placeholder = { label?.let { Text(it) } },
    isError = isError,
    keyboardOptions = keyboardOptions
  )

}