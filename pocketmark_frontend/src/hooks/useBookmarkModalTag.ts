import { useEffect, useState } from 'react';
import useEdit from './useEdit';

function useBookmarkModalTag() {
	const { editData } = useEdit();
	const [tag, setTag] = useState<any>({
		inputValue: '',
		value: [],
	});

	useEffect(() => {
		editData?.tags &&
			setTag({
				...tag,
				value: editData?.tags.map((b: any) => ({
					label: b.name,
					value: b.name,
				})),
			});
	}, [editData, tag]);

	const createOption = (label: any) => ({
		label,
		value: label,
	});

	const handleChange = (value: any) => {
		setTag({ ...tag, value: value });
	};

	const handleInputChange = (inputValue: any) => {
		setTag({ ...tag, inputValue: inputValue });
	};

	const handleKeyDown = (event: any) => {
		const { inputValue, value } = tag;
		if (!inputValue) return;
		switch (event.key) {
			case 'Enter':
			case 'Tab':
				!event.nativeEvent.isComposing &&
					setTag({
						inputValue: '',
						value: [...value, createOption(inputValue)],
					});
				event.preventDefault();
		}
	};

	function resetTag() {
		setTag({
			inputValue: '',
			value: [],
		});
	}

	return { tag, handleChange, handleInputChange, handleKeyDown, resetTag };
}

export default useBookmarkModalTag;
