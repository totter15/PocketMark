import { useState } from 'react';
import { TagSelectDataType } from '../interfaces/data';

function useTagSelect() {
	const initialState: TagSelectDataType = {
		inputValue: '',
		value: [],
	};
	const [tag, setTag] = useState(initialState);
	function tagHandler(tagData: TagSelectDataType) {
		setTag(tagData);
	}
	function resetTag() {
		setTag(initialState);
	}

	return { tag, tagHandler, resetTag };
}

export default useTagSelect;
