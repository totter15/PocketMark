import { useState, useEffect } from 'react';
import useEdit from './useEdit';

interface FormDataType {
	name: string;
	url: string;
	comment: string;
}

function useBookmarkModalData() {
	const initialData = {
		name: '',
		url: '',
		comment: '',
	};

	const { isEditBookmark, editData } = useEdit();
	const [formData, setFormData] = useState<FormDataType>(initialData);

	function resetFormData() {
		setFormData(initialData);
	}

	const formDataHandler = (e: any) => {
		setFormData({ ...formData, [e.target.name]: e.target.value });
	};

	useEffect(() => {
		if (isEditBookmark && editData) {
			const { name, url, comment } = editData ?? {};
			setFormData({ name, url, comment });
		}
	}, [editData, isEditBookmark]);

	return {
		formData,
		formDataHandler,
		resetFormData,
	};
}

export default useBookmarkModalData;
