import React from 'react';
import CreatableSelect from 'react-select/creatable';
import { TagSelectPropsType } from '../../interfaces/data';

function TagSelect({ tag, handleTag }: TagSelectPropsType) {
	function handleChange(value: any) {
		handleTag({ ...tag, value: value });
	}

	function handleInputChange(inputValue: string) {
		handleTag({ ...tag, inputValue: inputValue });
	}

	function createOption(label: string) {
		return { label, value: label };
	}

	const handleKeyDown = (event: any) => {
		const { inputValue, value } = tag;
		if (!inputValue) return;
		switch (event.key) {
			case 'Enter':
			case 'Tab':
				!event.nativeEvent.isComposing &&
					handleTag({
						inputValue: '',
						value: [...value, createOption(inputValue)],
					});
				event.preventDefault();
		}
	};

	return (
		<CreatableSelect
			inputValue={tag.inputValue}
			isClearable
			isMulti
			menuIsOpen={false}
			onChange={handleChange}
			onInputChange={handleInputChange}
			onKeyDown={handleKeyDown}
			placeholder='태그 입력'
			value={tag.value}
		/>
	);
}

export default TagSelect;
