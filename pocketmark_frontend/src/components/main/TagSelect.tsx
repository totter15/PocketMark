import React, { useEffect } from 'react';
import CreatableSelect from 'react-select/creatable';
import { TagSelectPropsType } from '../../interfaces/data';
import useEdit from '../../hooks/useEdit';

function TagSelect({ tag, handleTag }: TagSelectPropsType) {
	const { editData } = useEdit();

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

	useEffect(() => {
		if (editData) {
			const currentTag =
				editData?.tags?.map((tag: { name: string }) => ({
					value: tag.name,
					label: tag.name,
				})) ?? [];
			handleTag({ inputValue: '', value: currentTag });
		}
	}, [editData]);

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
